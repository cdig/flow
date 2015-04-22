(ns entity.line
  (:require [app.entity :as entity]
            [core.math :refer [round]]))


(defn- snap [[x y]]
  [(* (round (/ x 30)) 30)
   (* (round (/ y 30)) 30)])

(defn- snap-points [points]
  (vec (map snap points)))

(defn create!
  "Make and save a new line Entity."
  [& points]
  (entity/create! {:geo {:type :line :points (snap-points points)}
                   :dye {:stroke "#CCC"}}))

(defn edit!
  "Edit and save an existing line."
  [eid & points]
  (entity/update! eid assoc-in [:geo :points] (snap-points points)))

(defn move-tail!
  "Move the tail of an existing line."
  [eid new-tail]
  (entity/update! eid update-in [:geo :points] #(conj (pop %) (snap new-tail))))
