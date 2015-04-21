 ;; One app.cljs to rule them all!

(ns ^:figwheel-always app.app
  (:require [app.db :as db]
            [app.engine :as engine]
            [app.events :as events]
            [app.logics :as logics]
            [app.render :as render]
            [app.testem :as testem]
            [app.viewport :as viewport]
            [browser.all :as browser]
            [handlers.all :as handlers]))

(defn- tick! [dT]
  (-> (events/drain-queue!)
      (logics/update-world! dT)
      render/render-world!))

(defn- setup! [win]
  
  (events/setup!)
  
  ;; These two must run in this order, and come before the below
  (browser/setup! win)
  (handlers/setup!)
  
  (viewport/setup!)
  (testem/setup!)
  (engine/start! tick!))
  
(defn initialize! []
  (setup! js/window))
(defonce initialized (do (initialize!) true))
