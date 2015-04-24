(ns app.id)

(defn make-id
  "Returns a 2 element vector with the updated world, and a new unique(ish) id with the given prefix."
  [world prefix]
  (let [count (get world ::counter 0)
        id (keyword (str (name prefix) "-" count))
        new-world (assoc world ::counter (inc count))]
    [new-world id]))
