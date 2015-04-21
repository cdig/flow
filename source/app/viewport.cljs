(ns app.viewport
  (:require [app.db :as db]
            [graphics.grid :as grid]
            [browser.window :as window]))

(def merge+ (partial merge-with +))

(defn get-pos []
 (db/get-state ::pos))

(defn set-pos! [p]
 (db/set-state! ::pos p))

(defn move-pos! [rel]
 (db/update-state! ::pos merge+ rel))

(defn get-size []
  {:w (window/width) :h (window/height)})

(defn renderable []
  (grid/generate (merge (get-pos) (get-size))))

(defn setup! []
  (db/set-state! ::pos {:x 0 :y 0}))
