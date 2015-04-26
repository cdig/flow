(ns gui.grid-cursor
  (:require [app.thing :as thing]
            [core.math :refer [round]]))

(defn setup
  [world]
  (thing/create world {:tid ::grid-cursor
                       :grid-pos [0 0]
                       :circle 30
                       :dye {:stroke "rgba(0,0,0,0)"}}))

(defn show
  [world]
  (thing/attach-facets world ::grid-cursor {:dye {:stroke "hsl(212,24%,32%)"}}))

(defn hide
  [world]
  (thing/attach-facets world ::grid-cursor {:dye {:stroke "rgba(0,0,0,0)"}}))

(defn move
  [world event-data]
  (let [abs (:abs event-data)
        x (round (/ (:x abs) 30))
        y (round (/ (:y abs) 30))]
    (thing/attach-facets world ::grid-cursor {:grid-pos [x y]})))
