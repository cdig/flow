(ns gui.viewport
  (:require [gui.grid :as grid]
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

(defn get-size
  []
  {:w (window/width) :h (window/height)})

(defn renderable
  [world]
  (grid/generate (merge (get-pos world) (get-size))))
