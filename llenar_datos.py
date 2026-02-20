import requests
import random

# --- CONFIGURACIÓN ---
URL_API = "http://localhost:8080/api"
CANTIDAD_PRODUCTOS = 1000
# ---------------------

def obtener_ids_usuarios():
    print("\n🔍 Buscando usuarios existentes...")
    try:
        # Pedimos al backend la lista de todos los usuarios
        resp = requests.get(f"{URL_API}/users")
        if resp.status_code == 200:
            users = resp.json()
            ids = [u['id'] for u in users]
            print(f"   ✅ Se encontraron {len(ids)} usuarios en la base de datos.")
            return ids
    except:
        pass
    return []

def crear_categorias():
    print("🏗️ Creando Categorías...")
    nombres = ["Electrónica", "Hogar", "Ropa", "Deportes", "Libros"]
    ids = []
    for nombre in nombres:
        try:
            resp = requests.post(f"{URL_API}/categories", json={"name": nombre, "description": "Desc"})
            if resp.status_code in [200, 201]:
                ids.append(resp.json()['id'])
            else:
                # Si falla, asumimos IDs secuenciales
                ids.append(nombres.index(nombre) + 1)
        except:
            pass
    
    if not ids: return [1, 2, 3, 4, 5]
    return ids

def crear_productos(user_ids, cat_ids):
    print(f"\n📦 Generando {CANTIDAD_PRODUCTOS} Productos...")
    adjetivos = ["Pro", "Max", "Lite", "Ultra", "2026", "Gaming"]
    sustantivos = ["Laptop", "Mouse", "Teclado", "Monitor", "Silla"]
    
    contador = 0
    for i in range(1, CANTIDAD_PRODUCTOS + 1):
        u_id = random.choice(user_ids)
        c_id = random.choice(cat_ids)
        
        json_data = {
            "name": f"{random.choice(sustantivos)} {random.choice(adjetivos)} {i}",
            "description": "Producto generado",
            "price": round(random.uniform(10.0, 2500.0), 2),
            "stock": random.randint(0, 100),
            "userId": u_id,
            "categoryIds": [c_id]
        }
        
        try:
            r = requests.post(f"{URL_API}/products", json=json_data)
            if r.status_code in [200, 201]: contador += 1
            if i % 100 == 0: print(f"   📦 {i} productos procesados...")
        except: pass

    print(f"\n✨ ¡LISTO! {contador} productos creados. ✨")

if __name__ == "__main__":
    # 1. Asegurar Categorías
    cat_ids = crear_categorias()
    
    # 2. Obtener Usuarios (Los que ya tienes + nuevos si fuera necesario)
    user_ids = obtener_ids_usuarios()
    
    # 3. Si no hay usuarios, intentamos crear uno de emergencia
    if not user_ids:
        print("⚠️ No hay usuarios. Creando uno de emergencia...")
        try:
            r = requests.post(f"{URL_API}/users", json={"name":"Admin","email":"admin@test.com","password":"password123"})
            if r.status_code in [200, 201]: user_ids.append(r.json()['id'])
        except: pass

    # 4. Crear Productos
    if user_ids:
        crear_productos(user_ids, cat_ids)
    else:
        print("❌ Error crítico: No hay usuarios para asignar productos.")