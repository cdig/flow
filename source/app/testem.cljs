(ns app.testem
  (:require [app.entity :as entity]))

(defn- xpos [i]
  (-> i
      (mod 60)
      (* 25)))

(defn- ypos [i]
  (-> i
      (quot 60)
      (* 25)))

(dotimes [i 2000]
  (entity/create! {
    :pos {:x (xpos i) :y (ypos i)}
    :geo {:type :circle :r 5}
    :dye {:fill "rgba(255, 255, 255, 0.5)"
          :stroke "rgb(200, 200, 200)"}}))
