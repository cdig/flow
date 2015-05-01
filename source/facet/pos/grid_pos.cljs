(ns facet.pos.grid-pos)

;; Hack
(defn- scale [i] (* i 30))

;; PUBLIC

(defn create
  "Takes a position in grid units (unscaled) in the form {:x x :y y}. If nil, assumes 0 0."
  [pos]
  (if pos
      (zipmap
        (keys pos)
        (map scale (vals pos))) ;; Pre-scaling is a bit of a premature optimization. We should probably do it at render time, I think.
      {:x 0 :y 0}))

(defn render
  [world state]
  state)
