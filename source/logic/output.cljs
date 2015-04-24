(ns logic.output
  (:require [entity.entity :as entity]
            [entity.grid-cursor :as grid-cursor]
            [entity.line :as line]
            [gui.viewport :as viewport]))

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

; PUBLIC

(defn act
  [world event]
  (-> world
      (viewport-nav event)
      (draw-lines event)
      (show-cursor event)))
