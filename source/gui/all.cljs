(ns gui.all
  (:require [gui.grid :as grid]
            [gui.grid-cursor :as grid-cursor]))

(defn setup
  [world]
  (-> world
      grid/setup
      grid-cursor/setup))
