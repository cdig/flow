;; Side Effects X
;; State X
;; Deps √

(ns core.color
  (:require [core.math :refer [random round]]))

(defn rgb [r g b]
  (str "rgb(" r "," g "," b ")"))

(defn rgbn [r g b]
  (rgb (* r 255) (* g 255) (* b 255)))

(defn random-color []
  (let [color #(-> (random) (* 255) (round))]
    (rgb (color) (color) (color))))
