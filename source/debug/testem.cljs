(ns debug.testem
  (:require [entity.entity :as entity]
            [facet.facet :as facet]
            [core.color :refer [random-color]]))

(defn- create-circle
  [world i]
  (let [eid (entity/create!)]
    (-> world
        (facet/attach :user eid :grid-pos {:x (mod i 50) :y (quot i 50)})
        (facet/attach :user eid :circle 8)
        (facet/attach :user eid :random-fill nil)
        (facet/attach :user eid :stroke-rgb 40))))

(defn- test-circles
  [world]
  (loop [i (dec 1) world world]
    (if (neg? i)
        world
        (recur (dec i) (create-circle world i)))))

;; PUBLIC

(defn setup
  [world]
  (-> world
      test-circles))
