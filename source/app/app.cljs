 ;; One app.cljs to rule them all!

(ns ^:figwheel-always app.app
  (:require [app.engine :as engine]
            [app.events :as events]
            [app.id :as id]
            [app.undo :as undo]
            [browser.all :as browser]
            [entity.entity :as entity]
            [entity.grid-cursor :as grid-cursor]
            [handlers.all :as handlers]
            [logic.logics :as logics]
            [object.object :as object]
            [object.testem :as testem]
            [render.render :as render]))

(defonce world (atom nil))

(defn- save-world! [w] (reset! world w))
(defn- save-state! [w] (undo/save! (object/all w)) w)
(defn- safe-print [w] (print w) w)

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
      grid-cursor/setup
      object/setup
      testem/setup
      save-world!
      save-state!)
  (engine/start! tick!))
  
(defn initialize! []
  (setup! js/window))
(defonce initialized (do (initialize!) true))
