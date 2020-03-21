class HtmlUtil
{
  toggleDropdown = (dropdown, active) =>
  {
    if(dropdown instanceof HTMLElement)
    {
      const display = dropdown.style.display ?
          dropdown.style.display : "none";

      if(display === "none" && active === true)
        dropdown.style.display = "flex";
      else
        dropdown.style.display = "none";
    }
  }

  hideElement = (timeout) =>
  {
    
  }
}


const htmlUtil = new HtmlUtil();
export default htmlUtil;