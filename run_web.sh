# rm -rf docs/MASTG
rm -rf docs/MASWE
./src/scripts/structure_mastg.sh
python3 src/scripts/transform_files.py
python3 src/scripts/populate_dynamic_pages.py
# python3 src/scripts/generate_redirects.py
mkdocs serve -a localhost:8002