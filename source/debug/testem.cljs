(ns debug.testem
  (:require [object.object :as object]
            [app.thing :as thing]
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

(defn create-object
  [world i]
  (object/create
    world
    {:pos {:x (xpos i) :y (ypos i)}
     :geo {:type :circle :r 8}
     :dye {:fill (random-color)
           :stroke "rgb(40, 40, 40)"}}))

(defn create-thing
  [world i]
  (thing/create world {:grid-pos [(xpos i) (ypos i)]
                       :layer :user
                       :circle 8
                       :dye [:random-fill
                             [:stroke-rgb 40]]}))

(defn setup [world]
  (loop [i (dec 10) world world]
    (if (neg? i)
        world
        (recur (dec i) (create-object world i)))))
