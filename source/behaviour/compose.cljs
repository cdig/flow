(ns behaviour.compose
  (:require [app.system :refer [register-system!]]
            [app.behaviour :as behaviour]))

(defn create [entity state]
  (let [eid (:eid entity)
        sub-behaviours (:compose state)]
        
    ;; Reduce over the sub-behaviours, making a new behaviour instance for each.
    ;; This generates a new state map for the compose behaviour, mapping sub-behaviour bnames to bids.
    (reduce-kv
      
      ;; Arg 1: reducing function
      (fn [new-state sub-bname sub-state]
        (assoc new-state sub-bname (behaviour/add! eid sub-bname sub-state)))
      
      ;; Arg 2: a new empty collection of the same type to put results into
      (empty sub-behaviours)
      
      ;; Arg 3: collection to reduce over
      sub-behaviours)))

(register-system! :compose create :create)
