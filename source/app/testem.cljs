(ns app.testem
  (:require [app.entity :as entity]
            [core.color :refer [random-color]]))

(defn- xpos [i]
  (-> i
      (mod 50)
      (* 30)
      (+ 15)))

(defn- ypos [i]
  (-> i
      (quot 50)
      (* 30)
      (+ 15)))

(defn setup! []
  (dotimes [i 100]
    (entity/create! {
      :pos {:x (xpos i) :y (ypos i)}
      :geo {:type :circle :r 8}
      :dye {:fill (random-color)
            :stroke "rgb(40, 40, 40)"}})))
