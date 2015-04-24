 ;; One app.cljs to rule them all!

(ns ^:figwheel-always app.app
  (:require [entity.entity :as entity]
            [entity.grid-cursor :as grid-cursor]
            [entity.testem :as testem]
            [handlers.all :as handlers]
            [logic.logics :as logics]
            [render.render :as render]
            [app.engine :as engine]
            [app.events :as events]
            [app.id :as id]
            [browser.all :as browser]))

(defonce world (atom nil))

(defn- save-world! [new-world] (reset! world new-world))
(defn- safe-print [world] (print world) world)

(defn- tick! [dT]
  (-> @world
      (assoc :dT dT)
      events/drain!
      logics/act
      render/act!
      save-world!))

(defn- setup! [window]
  (browser/setup! window)
  (handlers/setup!)
  (-> {}
      entity/setup
      testem/setup
      grid-cursor/setup
      save-world!)
  (engine/start! tick!))
  
(defn initialize! []
  (setup! js/window))
(defonce initialized (do (initialize!) true))
