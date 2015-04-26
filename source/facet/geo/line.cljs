(ns facet.geo.circle)

;; PUBLIC

(defn create
  [[start end]]
  {:start start :end end})

(defn render
  [state]
  {:type :line :points []})
