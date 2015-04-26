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

(declare dispatch)

;; PUBLIC

(defn create
  "Creates a new facet instance and attaches it to the given entity. Takes a world, thing id, facet type, and initial facet value. Returns the updated world. If you would like to have a reference to the new facet, please include a facet id in the initial facet value."
  [world tid type init]
  (let [fid (or (:fid init) (ider/get-next! :fid))]
    (-> world
      (assoc-in [::things tid type] fid)
      (dispatch type fid init))))

;; STUPID

(defn- dispatch
  "Dispatches to the correct facet create function. Takes a world, facet type, facet id, and initial facet value. Returns the updated world."
  [world type fid init]
  (case type
    ;; Dye
    :dye (dye/create world fid init)
    :random-fill (random-fill/create world fid init)
    :stroke-rgb (stroke-rgb/create world fid init)
    
    ;; Geo
    :circle (circle/create world fid init)
    :geo (geo/create world fid init)
    :grid (grid/create world fid init)
    
    ;; Layer
    :layer (layer/create world fid init)
    
    ;; Pos
    :grid-pos (grid-pos/create world fid init)
    :pos (pos/create world fid init)
    ))
