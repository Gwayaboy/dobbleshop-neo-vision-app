import re

file_path = 'app/src/main/java/com/dobbleshop/neovision/ui/screens/DashboardScreen.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# Fix mock pet creation (lines 53-64 and 71-82 approx)
# Need to:
# 1. Change id from 0 to "0" (String)
# 2. Change weightKg from 4.5 to 4.5f (Float)
# 3. Add deviceId parameter

mock_pet_pattern = r'Pet\(\s+id = 0,\s+name = "Léo",\s+species = com\.dobbleshop\.neovision\.data\.model\.Species\.CAT,\s+breed = "Chat Domestique",\s+weightKg = 4\.5,\s+ageMonths = 36,\s+sex = com\.dobbleshop\.neovision\.data\.model\.Sex\.MALE,\s+isSterilized = true,\s+activityLevel = com\.dobbleshop\.neovision\.data\.model\.ActivityLevel\.NORMAL,\s+nutritionalGoal = com\.dobbleshop\.neovision\.data\.model\.NutritionalGoal\.MAINTAIN\s+\)'

replacement = """Pet(
                    id = "0",
                    name = "Léo",
                    species = com.dobbleshop.neovision.data.model.Species.CAT,
                    breed = "Chat Domestique",
                    weightKg = 4.5f,
                    ageMonths = 36,
                    sex = com.dobbleshop.neovision.data.model.Sex.MALE,
                    isSterilized = true,
                    activityLevel = com.dobbleshop.neovision.data.model.ActivityLevel.NORMAL,
                    nutritionalGoal = com.dobbleshop.neovision.data.model.NutritionalGoal.MAINTAIN,
                    deviceId = "dev_001"
                )"""

content = re.sub(mock_pet_pattern, replacement, content)

# Also fix the second occurrence (it might have slightly different indentation or comments)
# Let's use a more flexible regex for both

flexible_pattern = r'Pet\(\s*id = 0,\s*name = "Léo",[^)]+MAINTAIN\s*\)'
content = re.sub(flexible_pattern, replacement, content)

# Fix activePet nullability at line 248
# activePet is Pet? but ActiveAnimalCard expects Pet

# First, ensure activePet is not null when passed to ActiveAnimalCard
content = content.replace('ActiveAnimalCard(\n                pet = activePet,', 'activePet?.let {\n                ActiveAnimalCard(\n                    pet = it,')
content = content.replace('pet = activePet,', 'pet = activePet!!,') # Fallback if first replacement fails or matches multiple

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
