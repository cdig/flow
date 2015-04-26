(ns gui.grid-cursor
  (:require [entity.entity :as entity]
            [core.math :refer [round]]))

(defn- set-visibility
  [world]
  (if (= (:mode world) :drawing)
      (entity/attach-facets world ::grid-cursor {:dye {:stroke "hsla(212,24%,32%,1)"}})
      (entity/attach-facets world ::grid-cursor {:dye {:stroke "hsla(212,24%,32%,0)"}})))

(defn- check-move
  [world [event-type event-data]]
  (if (and (= (:mode world) :drawing)
           (or (= event-type :mouse-move) ;; We also want to add key-down here, but we need access to current mouse state.
               (= event-type :mouse-drag)))
      (let [{{x :x y :y} :abs} event-data]
        (entity/attach-facets world ::grid-cursor {:grid-pos [(round (/ x 30)) (round (/ y 30))]}))
      world))

;; PUBLIC

(defn setup
  [world]
  (entity/create world {:eid ::grid-cursor
                       :grid-pos [0 0]
                       :circle 30
                       :dye {:stroke "rgba(0,0,0,0)"}}))

(defn tick
  [world event]
  (-> world
      set-visibility
      (check-move event)))
