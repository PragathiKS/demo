#!/bin/bash
ssh-keygen -q -t rsa -N "" -f ~/.ssh/id_rsa 2>/dev/null <<< y >/dev/null
cat ~/.ssh/id_rsa.pub
