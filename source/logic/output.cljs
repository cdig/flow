(ns logic.output
  (:require [app.undo :as undo]
            [object.object :as object]
            [gui.grid-cursor :as grid-cursor]
            [object.line :as line]
            [gui.viewport :as viewport]))

;; I don't think I need prev-objects anymore, since undo/save! does a diff on prev
(defonce prev-objects (atom nil))

;; HELPERS

(defn save-state
  [world]
  (let [objects (object/all world)]
    (when (not= @prev-objects objects)
      (reset! prev-objects objects)
      (undo/save! objects))
    world))

(defn merge+
  [old new]
  (merge-with + old new))

(defn- move-viewport
  [world event-data]
  (viewport/move-pos world (:rel event-data)))

(defn- move-all-objects
  [world event-data]
  (let [objects (object/all world)]
    (loop [world world
           [oid ent] (first objects)
           objects (rest objects)]
      (if (nil? ent)
          world
          (recur
            (object/save world (update ent :pos merge+ (:rel event-data)))
            (first objects)
            (rest objects))))))

;; ACTIONS

(defn- viewport-nav
  [world [event-type event-data]]
  (if (and (= (:mode world) :navigating)
           (= event-type :mouse-drag))
      (-> world
          (move-viewport event-data)
          (move-all-objects event-data))
      world))

(defn- draw-lines
  [world [event-type event-data]]
  (or
    (case event-type

      :mouse-down (when (= (:mode world) :drawing)
                    (let [{{x :x y :y} :abs} event-data
                          [world e oid] (line/create-and-use world [x y] [x y])]
                      (assoc world ::drawing oid)))

      :mouse-drag (when-let [line-oid (::drawing world)]
                    (let [{{x :x y :y} :abs} event-data]
                      (line/move-tail world line-oid [x y])))

      :mouse-up (when-let [line-oid (::drawing world)]
                  (let [world (assoc world ::drawing nil)]
                    (if (zero? (line/length world line-oid))
                      (object/destroy world line-oid)
                      (save-state world))))

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
           (or (= event-type :mouse-move) ;; We also want to add key-down here, but we need access to current mouse state.
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
  (or
    (case (:action world)

      :undo
        (when-let [new-state (undo/undo!)]
          (->> new-state
               (reset! prev-objects)
               (object/populate world)))
      
      :redo
        (when-let [new-state (undo/redo!)]
          (->> new-state
               (reset! prev-objects)
               (object/populate world)))
      
      nil)
    world))

; PUBLIC

(defn- safe-print [world] (print world) world)

(defn act
  [world event]
  (-> world
      (viewport-nav event)
      (draw-lines event)
      (show-cursor event)
      handle-undo))
