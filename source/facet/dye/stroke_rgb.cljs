(ns facet.dye.stroke-rgb)

;; PUBLIC

(defn create
  [v]
  {:stroke (str "rgb(" v "," v "," v ")")})

(defn render
  [state]
  state)
