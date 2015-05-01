(ns facet.geo.circle)

;; PUBLIC

(defn create
  [r]
  {:type :circle :r r})

(defn render
  [world state]
  state)
