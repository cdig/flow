;; The render system is responsible for orchestrating how our entities get drawn to the screen. It takes the entities that we have, turns their data into drawing instructions, and feeds that data to a renderable surface, which does the side-effectful drawing work.

(ns app.render
  (:require [app.db :as db]
            [app.entity :as entity]
            [browser.window :as window]
            [graphics.surface :as surface]))

(defn- entity->renderable
  "Turns an entity into renderable data, which tells the surface what to draw."
  [[eid entity]]
  (merge
    (:pos entity)
    (:geo entity)
    (:dye entity)))

(defn render-world!
  "When our entities have changed, re-draw the screen. Currently only uses 1 layer."
  []
  (let [entities (entity/all)]
    (when-not (= entities (db/get-cache ::previous-entities))
      (db/set-cache! ::previous-entities entities)
      (surface/render!
        ::layer-0
        (window/width)
        (window/height)
        (map entity->renderable entities)))))
