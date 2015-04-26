;; Generates new IDs for use in the system.
;; For now, we don't care about saving/loading, or anything beyond the most minimal level of uniqueness.

(ns app.ider)

(defonce counter (atom 0))

(defn get-next!
  "Returns a new unique(ish) id with the given prefix."
  [prefix]
  (swap! counter inc)
  (keyword (str (name prefix) "-" @counter)))
