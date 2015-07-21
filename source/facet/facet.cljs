;;;; Facet
;; The facet namespace holds the creation and manipulation logic for all the facets that things in the system can be composed of.
;; Due to the lack of dynamic dispatch, this file is pretty exhaustive — but at least this gives a perf benefit.
;; Facets have a "type" which corresponds to the name of the file, and a "kind" which corresponds to the name of the folder.
;; The kinds govern the general concerns of behaviour in the system — simulation logic and rendering.
;; The types are specializations of these behaviours.

(ns facet.facet
  (:require [entity.entity :as entity]
            [facet.data.data :as data]
            [facet.data.eid :as eid]
            [facet.data.layer :as layer]
            [facet.dye.dye :as dye]
            [facet.dye.random-fill :as random-fill]
            [facet.dye.stroke-rgb :as stroke-rgb]
            [facet.geo.circle :as circle]
            [facet.geo.geo :as geo]
            [facet.geo.grid :as grid]
            [facet.geo.line :as line]
            [facet.pos.grid-pos :as grid-pos]
            [facet.pos.pos :as pos]
            ))

(declare dispatch-create)
(declare dispatch-render)

;; HELPERS

(defn- entity->renderable
  "Turns a entity into renderable data, which tells the surface what to draw."
  [world entity]
  (reduce merge (map (partial dispatch-render world) entity)))

;; PUBLIC

(defn attach
  [world entity-type eid facet-type state]
  (entity/store world entity-type eid (assoc (entity/fetch world entity-type eid) facet-type (dispatch-create facet-type state))))

; (defn change
;   [world entity-type eid facet-type function]
;   (entity/store world entity-type eid (update (entity/fetch world entity-type eid) facet-type function)))

(defn render
  [world entities]
  (map (partial entity->renderable world) (vals entities)))


;; STUPID

(defn- dispatch-create
  "Creates a new facet instance. Takes a facet type and initial facet value. Dispatches to the correct facet create function, since we don't have dynamic dispatch. Returns the new facet state."
  [type state]
  (case type
    
    ;; Data
    :data (data/create state)
    :eid (eid/create state)
    :layer (layer/create state)
    
    ;; Dye
    :dye (dye/create state)
    :random-fill (random-fill/create state)
    :stroke-rgb (stroke-rgb/create state)
    
    ;; Geo
    :circle (circle/create state)
    :geo (geo/create state)
    :grid (grid/create state)
    :line (line/create state)
    
    ;; Pos
    :grid-pos (grid-pos/create state)
    :pos (pos/create state)
    
    ;; No default value — if we use an unknown value, that's an error!
    ))

(defn- dispatch-render
  [world [type state]]
  (case type
    
    ;; Data
    :data (data/render world state)
    :eid (eid/render world state)
    :layer (layer/render world state)

    ;; Dye
    :dye (dye/render world state)
    :random-fill (random-fill/render world state)
    :stroke-rgb (stroke-rgb/render world state)
    
    ;; Geo
    :circle (circle/render world state)
    :geo (geo/render world state)
    :grid (grid/render world state)
    :line (line/render world state)
    
    ;; Pos
    :grid-pos (grid-pos/render world state)
    :pos (pos/render world state)
    
    ;; No default value — if we use an unknown value, that's an error!
    ))
