from fontTools.ttLib import TTFont
from copy import deepcopy
import random
import sys
import json

input_file = './Ancient_Cube-Regular.woff2'
map_file = './map.json'
output_format = 'woff2'
output_file = './RAC.woff2'

remapping_range = range(0x3400, 0x9FCB)

def create_bijection_mapping(original_list, seed=None):
    if seed is not None:
        random.seed(seed)
    
    shuffled_list = original_list[:]
    random.shuffle(shuffled_list)
    
    mapping = {key: value for key, value in zip(original_list, shuffled_list)}
    return mapping

def get_uniid(unicode_id):
    return f'uni{unicode_id:X}'

def from_uniid(uniid):
    try:
        return int(uniid[3:], 16)
    except:
        return -1

#font = TTFont(input_file)
#mapping_range = [k for k in list(font['glyf'].glyphs.keys()) if k.startswith('uni') and from_uniid(k) in remapping_range]
#mapping = create_bijection_mapping(mapping_range, seed=42) # This mapping should be fixed!

with open(map_file, "r") as f:
    mapping = json.loads(f.read())
rev_mapping = {value: key for key, value in mapping.items()}


def create_mapped_font():
    font = TTFont(input_file)
    origin_glyf = deepcopy(font['glyf'].glyphs)
    for from_id, to_id in mapping.items():
        font['glyf'].glyphs[from_id] = origin_glyf[to_id]
    origin_hmtx = deepcopy(font['hmtx'].metrics)
    for from_id, to_id in mapping.items():
        font['hmtx'].metrics[from_id] = origin_hmtx[to_id]
    origin_vmtx = deepcopy(font['vmtx'].metrics)
    for from_id, to_id in mapping.items():
        font['vmtx'].metrics[from_id] = origin_vmtx[to_id]
    font.flavor = output_format
    font.save(output_file)


def map_text(text):
    return_text = ''
    for char in text:
        if ord(char) not in remapping_range:
            return_text += char
        else:
            return_text += chr(from_uniid(rev_mapping[get_uniid(ord(char))]))
    return return_text

def save_mapping():
    with open(map_file, "w") as f:
        f.write(json.dumps(mapping))

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print('Usage: python remap.py -t <text>')
        sys.exit(1)

    if sys.argv[1] == '-t':
        print(map_text(sys.argv[2]))
    if sys.argv[1] == '-m':
        create_mapped_font()

