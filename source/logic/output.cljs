(ns logic.output
  (:require [app.undo :as undo]
            [object.object :as object]
            [gui.grid-cursor :as grid-cursor]
            [object.line :as line]
            [gui.viewport :as viewport]))

;; HELPERS

(defn save-state
  [world]
  (undo/save! (object/all world))
  world)

;; ACTIONS

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

(defn handle-undo
  [world]
  (or
    (case (:action world)

      :undo
        (when-let [new-state (undo/undo!)]
          (object/populate world new-state))
      
      :redo
        (when-let [new-state (undo/redo!)]
          (object/populate world new-state))
      
      nil)
    world))

; PUBLIC

(defn act
  [world event]
  (-> world
      (viewport/tick event)
      (draw-lines event)
      (grid-cursor/tick event)
      handle-undo))
