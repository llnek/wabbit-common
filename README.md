# wabbit-svcs

Wabbit's built-in emitter definitions.

## Usage

    (require '[czlab.wabbit.svcs.core :as wc])

    (let [types (wc/emittersByService)]
      (doseq [[k v] types]
        (println "service = " k)
        (println "info map = " (:info v))
        (println "config map = " (:conf v))))

    (let [types (wc/emittersByType)]
      (doseq [[k v] types]
        (println "type = " k)
        (println "info map = " (:info v))
        (println "config map = " (:conf v))))


## License

Copyright © 2013-2017 Kenneth Leung

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
