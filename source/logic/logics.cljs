;; Based on the GUI instructions from the inputs processing, update all the entities in the world.

(ns logic.logics
  (:require [gui.viewport :as viewport]
            [gui.grid-cursor :as grid-cursor]
            [logic.draw-lines :as draw-lines]
            [logic.handle-undo :as handle-undo]
            [logic.process-input :as process-input]
            [logic.scale-grid :as scale-grid]
            [logic.window-size :as window-size]))

(defn- safe-print [v] (print v) v)

;; PUBLIC

(defn act
  "Update the world based on the given event. Here's where we apply all the level0 business logic."
  [world]
  (let [event (:event world)]
    (-> world
        
        ;; Must come first
        (process-input/tick event)
        
        ;; Order doesn't matter
        (draw-lines/tick event)
        (grid-cursor/tick event)
        (handle-undo/tick event)
        (scale-grid/tick event)
        (viewport/tick event)
        (window-size/tick event)
        
        ;; Finally, clear out any temporary state created above that doesn't need to live on.
        (assoc :action nil))))
