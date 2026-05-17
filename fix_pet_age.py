import sys

with open('app/src/main/java/com/dobbleshop/neovision/ui/screens/DashboardScreen.kt', 'r', encoding='utf-8') as f:
    content = f.read()

# Replace ${pet.ageYears} with ${(pet.ageMonths ?: 0) / 12}
content = content.replace('${pet.ageYears}', '${(pet.ageMonths ?: 0) / 12}')

with open('app/src/main/java/com/dobbleshop/neovision/ui/screens/DashboardScreen.kt', 'w', encoding='utf-8') as f:
    f.write(content)
