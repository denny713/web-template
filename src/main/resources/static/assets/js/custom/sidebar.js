function generateMenu() {
    let access = getAccessList();
    let ul = document.getElementById("menu");
    for (let x in access) {
        let id = access[x].menuId;
        let li = document.createElement("li");
        li.setAttribute("id", id);
        let aMenu = document.createElement("a");
        aMenu.setAttribute("id", "a" + id);
        aMenu.setAttribute("href", access[x].menuUrl);
        aMenu.setAttribute("class", "nav-link px-0 align-middle sidebar-menu");
        let iMenu = document.createElement("i");
        iMenu.setAttribute("class", "fas " + access[x].menuIcon);
        aMenu.appendChild(iMenu);
        let spanMenu = document.createElement("span");
        spanMenu.setAttribute("class", "ms-1 d-none d-sm-inline");
        spanMenu.innerHTML = access[x].menuName;
        aMenu.appendChild(spanMenu);
        li.appendChild(aMenu);
        ul.appendChild(li);
    }
}