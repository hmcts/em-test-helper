#!/bin/bash
date
docker-compose -f docker-compose-dependencies.yml logs -f --tail=1


