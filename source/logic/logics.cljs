;; Based on the GUI instructions from the inputs processing, update all the entities in the world.

(ns logic.logics
  (:require [logic.inputs :as inputs]
            [logic.output :as output]))

(defn- process-event
  [world event]
  (-> world
      (inputs/act event)
      (output/act event)))

;; MAIN

(defn act
  "Loop over all the events, and update the world accordingly."
  [world]
  (loop [world world event-log (:event-log world)]
    (if-let [event (first event-log)]
      (recur (process-event world event) (rest event-log))
      world)))
