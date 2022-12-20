using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using LayerDAL;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;

namespace LayerBLL.Logics
{
    public class PlanonutricionalLogic
    {
        public static async Task<Response> PlanonutricionaisLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<PlanoNutricional> planonutricionalList = await LayerDAL.PlanonutricionalService.GetPlanoNutricionaisService(sqlDataSource);
            if (planonutricionalList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Sucesso na obtenção dos dados";
                response.Data = new JsonResult(planonutricionalList);
            }
            return response;
        }
    }
}
