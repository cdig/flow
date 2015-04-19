;; Based on the events, and the current state of the app, update entities.

(ns app.logics
  (:require [app.entity :as entity]
            [app.db :as db]))

; (defn update-world! [events]
;   (doseq [[eid entity] (entity/all)]
;
;     ;; APPLY LOGIC POWERFORCE
;
;     (doseq [[type event] events]
;       (when (= type :mouse-drag)
;         (entity/update! eid update :pos (partial merge-with +) (:rel event))))))


(def merge+ (partial merge-with +))

(defn update-world! [events]
  ;; APPLY LOGIC POWERFORCE
  
  (let [entities (entity/all)]
    (doseq [[type {:keys [rel]}] events]
      (when (= type :mouse-drag)
        (doseq [[eid entity] entities]
          (swap! db/state update-in [:app.entity/entities eid :pos] merge+ rel))))))
