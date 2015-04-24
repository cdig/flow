(ns logic.output
  (:require [app.undo :as undo]
            [entity.entity :as entity]
            [entity.grid-cursor :as grid-cursor]
            [entity.line :as line]
            [gui.viewport :as viewport]))

(defonce prev-entities (atom nil))

;; PERF HELPERS

(defn merge+
  [old new]
  (merge-with + old new))

(defn- move-viewport
  [world event-data]
  (viewport/move-pos world (:rel event-data)))

(defn- move-all-entities
  [world event-data]
  (let [entities (entity/all world)]
    (loop [world world
           [eid ent] (first entities)
           entities (rest entities)]
      (if (nil? ent)
          world
          (recur
            (entity/save world (update ent :pos merge+ (:rel event-data)))
            (first entities)
            (rest entities))))))

;; ACTIONS

(defn- viewport-nav
  [world [event-type event-data]]
  (if (and (= (:mode world) :navigating)
           (= event-type :mouse-drag))
      (-> world
          (move-viewport event-data)
          (move-all-entities event-data))
      world))

(defn- draw-lines
  [world [event-type event-data]]
  (or
    (case event-type

      :mouse-down (when (= (:mode world) :drawing)
                    (let [{{x :x y :y} :abs} event-data
                          [w e eid] (line/create-and-use world [x y] [x y])]
                      (assoc w ::drawing eid)))

      :mouse-drag (when-let [line-eid (::drawing world)]
                    (let [{{x :x y :y} :abs} event-data]
                      (line/move-tail world line-eid [x y])))

      :mouse-up (when-let [line-eid (::drawing world)]
                  (let [w (assoc world ::drawing nil)]
                    (if (zero? (line/length world line-eid))
                      (entity/destroy w line-eid)
                      w)))

      nil)
    world))

(defn- set-cursor
  [world]
  (if (= (:mode world) :drawing)
      (grid-cursor/show world)
      (grid-cursor/hide world)))

(defn- move-cursor
  [world [event-type event-data]]
  (if (and (= (:mode world) :drawing)
           (or (= event-type :mouse-move)
               (= event-type :mouse-drag)))
      (grid-cursor/move world event-data)
      world))

(defn- show-cursor
  [world event]
  (-> world
      set-cursor
      (move-cursor event)))

(defn handle-undo
  [world]
  (case (:action world)

    :undo
      (->> (undo/undo!)
           (reset! prev-entities)
           (entity/populate world))
    
    :redo
      (->> (undo/redo!)
           (reset! prev-entities)
           (entity/populate world))
    
    (let [entities (entity/all world)]
      (when (not= @prev-entities entities)
        (reset! prev-entities entities)
        (undo/save! entities))
      world)))

; PUBLIC

(defn- safe-print [world] (print world) world)

(defn act
  [world event]
  (-> world
      (viewport-nav event)
      (draw-lines event)
      (show-cursor event)
      handle-undo
      ))
