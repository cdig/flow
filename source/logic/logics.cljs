;; Based on the GUI instructions from the inputs processing, update all the entities in the world.

(ns logic.logics
  (:require [gui.viewport :as viewport]
            [gui.grid-cursor :as grid-cursor]
            [logic.draw-lines :as draw-lines]
            [logic.handle-undo :as handle-undo]
            [logic.process-input :as process-input]))

(defn- process-event
  "Update the world based on the given event."
  [world event]
  (-> world
      ;; Must come first
      (process-input/tick event)
      
      ;; Order doesn't matter
      (handle-undo/tick event)
      (draw-lines/tick event)
      (grid-cursor/tick event)
      (viewport/tick event)))

(defn- process-events
  "Loop over all the events, and update the world accordingly."
  [world]
  (loop [world world event-log (:event-log world)]
    (if-let [event (first event-log)]
      (recur (process-event world event) (rest event-log))
      world)))

(defn- cleanup
  "Clear out any temporary state created during logic that doesn't need to live on."
  [world]
  (assoc world :action nil))

;; PUBLIC

(defn act
  "Here's where we apply all the level0 business logic."
  [world]
  (-> world
      process-events
      cleanup))
