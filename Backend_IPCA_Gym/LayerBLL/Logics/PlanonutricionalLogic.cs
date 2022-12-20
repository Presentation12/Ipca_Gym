using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using LayerDAL.Services;

namespace LayerBLL.Logics
{
    public class PlanoNutricionalLogic
    {
        public static async Task<Response> PlanoNutricionaisLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<PlanoNutricional> planonutricionalList = await PlanoNutricionalService.GetAllService(sqlDataSource);
            
            if (planonutricionalList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult(planonutricionalList);
            }

            return response;
        }
    }
}
