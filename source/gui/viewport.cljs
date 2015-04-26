(ns gui.viewport
  (:require [facet.geo.grid :as grid]
            [browser.window :as window]))

(def merge+ (partial merge-with +))
(def empty-pos {:x 0 :y 0})

(defn get-pos
  [world]
  (or (:viewport world)
      empty-pos))

(defn set-pos
  [world p]
  (assoc world :viewport p))

(defn move-pos
  [world rel]
  (update world :viewport merge+ rel))

(defn tick
  [world [event-type event-data]]
  (if (and (= (:mode world) :navigating)
           (= event-type :mouse-drag))
      (move-pos world (:rel event-data))
      world))

(defn get-size
  []
  {:w (window/width) :h (window/height)})

(defn renderable
  [world]
  (grid/generate (merge (get-pos world) (get-size))))
