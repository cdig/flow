;; Based on the events, and the current state of the app, update entities.

(ns app.logics
  (:require [app.db :as db]
            [app.entity :as entity]
            [app.viewport :as viewport]
            [core.math :refer [round]]
            [entity.line :as line]))

;; PERF HELPERS

(def merge+ (partial merge-with +))

;; LOGICS

(defn drag-viewport
  [event-type event-data]
  (when (and (keyword-identical? event-type :mouse-drag)
             (not (db/get-cache ::ready-to-draw)))
    
    ;; Move the viewport
    (viewport/move-pos! (:rel event-data))

    ;; Move all entities
    (doseq [[eid entity] (entity/all)]
      (entity/update! eid update :pos merge+ (:rel event-data)))))


(defn- update-drawing-hint
  [type data]
  (when (and (db/get-cache ::ready-to-draw)
             (or (keyword-identical? :mouse-move type)
                 (keyword-identical? :mouse-drag type)))
    (let [abs (:abs data)
          x (* (round (/ (:x abs) 30)) 30)
          y (* (round (/ (:y abs) 30)) 30)]
      (entity/update! :drawing-hint assoc :pos {:x x :y y}))))
    

(defn- draw-line
  "Draw a line!"
  [type data]
  (case type
    :key-down (when (keyword-identical? :c (first data))
                (db/set-cache! ::ready-to-draw true)
                (entity/update! :drawing-hint assoc-in [:dye :fill] "#2A2A2A"))
                
    :key-up (when (keyword-identical? :c (first data))
              (db/set-cache! ::ready-to-draw false)
              (entity/update! :drawing-hint assoc-in [:dye :fill] "rgba(0,0,0,0)"))
              
    :mouse-down (when (db/get-cache ::ready-to-draw)
                  (let [{{x :x y :y} :abs} data]
                    (db/set-cache! ::drawing (line/create! [x y] [x y]))))
    :mouse-drag (when-let [line-eid (db/get-cache ::drawing)]
                  (let [{{x :x y :y} :abs} data]
                    (line/move-tail! line-eid [x y])))
    :mouse-up (when-let [line-eid (db/get-cache ::drawing)]
                (when (zero? (line/length line-eid))
                  (entity/destroy! line-eid))
                (db/set-cache! ::drawing nil))
    nil))
  
;; MAIN

(defn setup!
  []
  (entity/create! {:eid :drawing-hint
                   :geo {:type :circle :r 14}}))

(defn update-world!
  "Loop over all the events, and update the world accordingly."
  [[event-log event-set] dT]
  
  (doseq [[type data] event-log]
    (drag-viewport type data)
    (draw-line type data)
    (update-drawing-hint type data)
    
    (when (and (keyword-identical? :key-down type)
               (keyword-identical? :q (first data)))
      (doseq [[eid entity] (entity/all)]
        (entity/update! eid assoc-in [:dye :stroke] "#333")))

    )
  ;; The renderer will use the event-set to check for resize
  event-set)
