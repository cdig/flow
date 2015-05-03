(ns logic.handle-undo
  (:require [app.undo :as undo]
            [entity.entity :as entity]))

;; PUBLIC

(defn tick
  [world _]
  (or
    (case (:action world)

      :undo
        (when-let [new-state (undo/undo!)]
          (entity/populate world :user new-state))

      :redo
        (when-let [new-state (undo/redo!)]
          (entity/populate world :user new-state))

      nil)
    world))
