(ns facet.pos.grid-pos)

(defn- xpos
  [i]
  (* i 30))

(defn- ypos
  [i]
  (* i 30))

;; PUBLIC

(defn create
  [[x y]]
  {:x (xpos x) :y (ypos y)})

(defn render
  "Transforms posiiton data for rendering. No transformation needed, so far. In the future, we will need to handle stacked space transforms.. somewhere."
  [state]
  state)
