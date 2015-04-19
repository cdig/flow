(ns app.id
  (:require [app.db :as db]))

(defn make-id!
  "Returns a new unique id, with the given prefix."
  [prefix]
  (let [key (keyword (namespace ::x) (name prefix))
        val (or (db/get-state key) 0)
        id (keyword (str (name prefix) "-" val))]
    (db/set-state! key (inc val))
    id))
