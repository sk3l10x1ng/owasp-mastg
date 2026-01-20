#!/bin/bash
frida -U -f org.owasp.mastestapp.MASTestApp-iOS -l ./frida_permission_tracer.js -o output.txt
