;;;; Facet
;; The facet namespace holds the creation and manipulation logic for all the facets that things in the system can be composed of.
;; Due to the lack of dynamic dispatch, this file is pretty exhaustive — but at least this gives a perf benefit.
;; Facets have a "type" which corresponds to the name of the file, and a "kind" which corresponds to the name of the folder.
;; The kinds govern the general concerns of behaviour in the system — simulation logic and rendering.
;; The types are specializations of these behaviours.

(ns facet.all
  (:require [app.ider :as ider]
            [facet.dye.dye :as dye]
            [facet.dye.random-fill :as random-fill]
            [facet.dye.stroke-rgb :as stroke-rgb]
            [facet.geo.circle :as circle]
            [facet.geo.geo :as geo]
            [facet.geo.grid :as grid]
            [facet.layer.layer :as layer]
            [facet.pos.grid-pos :as grid-pos]
            [facet.pos.pos :as pos]
            ))

;; PUBLIC

(defn create
  "Creates a new facet instance. Takes a facet type and initial facet value. Dispatches to the correct facet create function, since we don't have dynamic dispatch. Returns the new facet state."
  [type init]
  (case type
    
    ;; Dye
    :dye (dye/create init)
    :random-fill (random-fill/create init)
    :stroke-rgb (stroke-rgb/create init)
    
    ;; Geo
    :circle (circle/create init)
    :geo (geo/create init)
    :grid (grid/create init)
    
    ;; Layer
    :layer (layer/create init)
    
    ;; Pos
    :grid-pos (grid-pos/create init)
    :pos (pos/create init)
    
    ;; No default value — if we use an unknown value, that's an error!
    ))

(defn render
  "Takes the type and state of a facet. Calls the render function. Returns any data needed for rendering, or nil."
  [type state]
  (case type
    
    ;; Dye
    :dye (dye/render state)
    :random-fill (random-fill/render state)
    :stroke-rgb (stroke-rgb/render state)
    
    ;; Geo
    :circle (circle/render state)
    :geo (geo/render state)
    :grid (grid/render state)
    
    ;; Layer
    :layer (layer/render state)
    
    ;; Pos
    :grid-pos (grid-pos/render state)
    :pos (pos/render state)
    
    ;; No default value — if we use an unknown value, that's an error!
    ))
