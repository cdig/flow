(ns gui.grid-cursor
  (:require [entity.entity :as entity]
            [core.math :refer [round]]))

(defn setup
  [world]
  (entity/create world {:eid ::drawing-hint
                        :geo {:type :circle :r 14}}))

(defn show
  [world]
  (entity/change world ::drawing-hint assoc-in [:dye :fill] "#2A2A2A"))

(defn hide
  [world]
  (entity/change world ::drawing-hint assoc-in [:dye :fill] "rgba(0,0,0,0)"))

(defn move
  [world event-data]
  (let [abs (:abs event-data)
        x (* (round (/ (:x abs) 30)) 30)
        y (* (round (/ (:y abs) 30)) 30)]
    (entity/change world ::drawing-hint assoc :pos {:x x :y y})))

      
