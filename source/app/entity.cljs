(ns app.entity
  (:require [app.db :refer [next-id! state]]))

(defn create! [name?]
  (let [eid (or name? (next-id! :eid))]
    (reset! state
      (assoc-in @state [:entities eid]
        (merge
          (@state :default-entity)
          {:eid eid})))
    eid))
