file_path = 'app/src/main/java/com/dobbleshop/neovision/ui/screens/DashboardScreen.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    lines = f.readlines()

new_lines = []
skip_until = -1
for i, line in enumerate(lines):
    if i < skip_until:
        continue
    
    # Looking for the broken block
    if 'activePet?.let {' in line and i + 1 < len(lines) and 'ActiveAnimalCard(' in lines[i+1]:
        # We found the block:
        # 249: activePet?.let {
        # 250:     ActiveAnimalCard(
        # 251:         pet = it,
        # 252:     onChangePet = { showAnimalDialog = true }
        # 253: )
        # It's missing a closing brace for the let block.
        new_lines.append(line) # activePet?.let {
        new_lines.append(lines[i+1]) # ActiveAnimalCard(
        new_lines.append(lines[i+2]) # pet = it,
        new_lines.append(lines[i+3]) # onChangePet = ...
        new_lines.append('                )\n') # closing ActiveAnimalCard
        new_lines.append('            }\n') # closing let
        skip_until = i + 4
    else:
        new_lines.append(line)

with open(file_path, 'w', encoding='utf-8') as f:
    f.writelines(new_lines)
