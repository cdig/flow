;; Based on the events, and the current state of the app, update entities.

(ns app.logics
  (:require [app.db :as db]
            [app.entity :as entity]
            [app.viewport :as viewport]
            [entity.line :as line]))

;; PERF HELPERS

(def merge+ (partial merge-with +))

;; LOGICS

(defn test-dragging
  "A simple test to see that dragging works"
  [event-type event-data]
  (when (keyword-identical? event-type :mouse-drag)
    
    ;; Drag the viewport
    (viewport/move-pos! (:rel event-data))
    
    ;; Drag all entities
    (doseq [[eid entity] (entity/all)]
      (entity/update! eid update :pos merge+ (:rel event-data)))))

(defn- draw-line
  "Draw a line!"
  [type data]
  (case type
    :mouse-down (let [{{x :x y :y} :abs} data]
                  (db/set-cache! ::drawing (line/create! [x y] [x y])))
    :mouse-drag (let [{{x :x y :y} :abs} data]
                  (line/move-tail! (db/get-cache ::drawing) [x y]))
    :mouse-up (db/set-cache! ::drawing nil)
    nil
    ))
  
;; MAIN

(defn update-world!
  "Loop over all the events, and update the world accordingly."
  [[event-log event-set] dT]
  
  (doseq [[type data] event-log]
    ; (test-dragging type data)
    (draw-line type data)
    )
  ;; The renderer will use the event-set to check for resize
  event-set)
