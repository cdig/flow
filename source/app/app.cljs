 ;; One app.cljs to rule them all!

(ns ^:figwheel-always app.app
  (:require [entity.entity :as entity]
            [entity.grid-cursor :as grid-cursor]
            [entity.testem :as testem]
            [handlers.all :as handlers]
            [logic.logics :as logics]
            [render.render :as render]
            [system.engine :as engine]
            [system.events :as events]
            [system.id :as id]
            [web.all :as web]))

(defonce world (atom {}))

(defn- save-world!
  [new-world]
  (reset! world new-world))

(defn- safe-print
  [world]
  (print world)
  world)

(defn- tick! [dT]
  (-> @world
      (assoc :dT dT)
      events/drain!
      logics/act
      render/act!
      save-world!
      ))

(defn- setup! [window]
  
  ;; These two must run in this order, and come before the below
  (web/setup! window)
  (handlers/setup!)
  
  (-> @world
      entity/setup
      testem/setup
      grid-cursor/setup
      save-world!
      )
  
  ;; We wrap tick! in an anonymous fn so that we can change it during dev and see our changes immediately.
  (engine/start! #(tick! %)))
  
(defn initialize! []
  (setup! js/window))
(defonce initialized (do (initialize!) true))
