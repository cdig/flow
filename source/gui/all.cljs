(ns gui.all
  (:require [gui.grid-cursor :as grid-cursor]))

(defn setup
  [world]
  (-> world
      grid-cursor/setup))
