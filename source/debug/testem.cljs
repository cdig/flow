(ns debug.testem
  (:require [object.object :as object]
            [app.thing :as thing]
            [core.color :refer [random-color]]))

(defn- xpos "DEPRECATED" [i]
  (-> i
      (mod 50)
      (* 30)
      (+ 15)))

(defn- ypos "DEPRECATED" [i]
  (-> i
      (quot 50)
      (* 30)
      (+ 15)))

(defn create-object
  "DEPRECATED"
  [world i]
  (object/create
    world
    {:pos {:x (xpos i) :y (ypos i)}
     :geo {:type :circle :r 8}
     :dye {:fill (random-color)
           :stroke "rgb(40, 40, 40)"}}))

(defn create-thing
  [world i]
  (thing/create world {:grid-pos [(mod i 50) (quot i 50)]
                       :circle 8
                       :random-fill nil
                       :stroke-rgb 40}))

(defn setup [world]
  (loop [i (dec 1) world world]
    (if (neg? i)
        world
        (recur (dec i) (create-thing world i)))))
